package delta2.system.whardwareinfo.hardwareinfo.Hardware;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;

import delta2.system.common.Helper;

public class CpuStat {

    private RandomAccessFile statFile;
    private CpuInfo mCpuInfoTotal;
    private ArrayList<CpuInfo> mCpuInfoList;

    public CpuStat() {
    }

    public void update() {
        try {
            createFile();
            parseFile();
            closeFile();
        } catch (Exception e) {
            statFile = null;
            Helper.Ex2Log(e);
        }
    }

    private void createFile() throws FileNotFoundException {
        statFile = new RandomAccessFile("/proc/stat", "r");
    }

    public void closeFile() throws IOException {
        if (statFile != null)
            statFile.close();
    }

    private void parseFile() {
        if (statFile != null) {
            try {
                statFile.seek(0);
                String cpuLine = "";
                int cpuId = -1;
                do {
                    cpuLine = statFile.readLine();
                    parseCpuLine(cpuId, cpuLine);
                    cpuId++;
                } while (cpuLine != null);
            } catch (IOException e) {
                Helper.Ex2Log(e);
            }
        }
    }

    private void parseCpuLine(int cpuId, String cpuLine) {
        if (cpuLine != null && cpuLine.length() > 0) {
            String[] parts = cpuLine.split("[ ]+");
            String cpuLabel = "cpu";
            if (parts[0].indexOf(cpuLabel) != -1) {
                createCpuInfo(cpuId, parts);
            }
        }
    }

    private void createCpuInfo(int cpuId, String[] parts) {
        if (cpuId == -1) {
            if (mCpuInfoTotal == null)
                mCpuInfoTotal = new CpuInfo();
            mCpuInfoTotal.update(parts);
        } else {
            if (mCpuInfoList == null)
                mCpuInfoList = new ArrayList<CpuInfo>();
            if (cpuId < mCpuInfoList.size())
                mCpuInfoList.get(cpuId).update(parts);
            else {
                CpuInfo info = new CpuInfo();
                info.update(parts);
                mCpuInfoList.add(info);
            }
        }
    }

    public int getCpuUsage(int cpuId) {
        update();
        int usage = 0;
        if (mCpuInfoList != null) {
            int cpuCount = mCpuInfoList.size();
            if (cpuCount > 0) {
                cpuCount--;
                if (cpuId == cpuCount) { // -1 total cpu usage
                    usage = mCpuInfoList.get(0).getUsage();
                } else {
                    if (cpuId <= cpuCount)
                        usage = mCpuInfoList.get(cpuId).getUsage();
                    else
                        usage = -1;
                }
            }
        }
        return usage;
    }


    public int getTotalCpuUsage() {
        update();
        int usage = 0;
        if (mCpuInfoTotal != null)
            usage = mCpuInfoTotal.getUsage();
        return usage;
    }


    public String toString() {
        update();
        StringBuffer buf = new StringBuffer();
        if (mCpuInfoTotal != null) {
            buf.append("CPU Total : ");
            buf.append(mCpuInfoTotal.getUsage());
            buf.append("%");
        }
        if (mCpuInfoList != null) {
            for (int i=0; i < mCpuInfoList.size(); i++) {
                CpuInfo info = mCpuInfoList.get(i);
                buf.append("\n\t\tCpu Core(" + i + ") : ");
                buf.append(info.getUsage());
             //   buf.append( " ("  + String.valueOf(getCPUFrequencyCurrent(i)) + ")" );


                buf.append("%");
                info.getUsage();
            }
        }

        float temp = getCpuTemp();
        if (temp>0)
            buf.append( "\n CPU Temp : " +  String.valueOf(temp) + "°C");

        return buf.toString();
    }


    /**/
    public float getCpuTemp() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = reader.readLine();
            float temp = Float.parseFloat(line) ;/// 1000.0f;

            if (temp > 1000 )
                temp /=1000;

            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    /**/

    DecimalFormat format = new DecimalFormat("##.00");
    private String getCPUFrequencyCurrent(int core) {

        Process p;
        try {
            p = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + String.valueOf(core) + "/cpufreq/scaling_cur_freq");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = reader.readLine();
            return String.valueOf (Integer.parseInt(line) / 1000000.00f) + "Mhz";


          //  return crr;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**/

    public class CpuInfo {
        private int mUsage;
        private long mLastTotal;
        private long mLastIdle;

        public CpuInfo() {
            mUsage = 0;
            mLastTotal = 0;
            mLastIdle = 0;
        }

        private int getUsage() {
            return mUsage;
        }


        public void update(String[] parts) {
            // the columns are:
            //
            //      0 "cpu": the string "cpu" that identifies the line
            //      1 user: normal processes executing in user mode
            //      2 nice: niced processes executing in user mode
            //      3 system: processes executing in kernel mode
            //      4 idle: twiddling thumbs
            //      5 iowait: waiting for I/O to complete
            //      6 irq: servicing interrupts
            //      7 softirq: servicing softirqs
            //
            long idle = Long.parseLong(parts[4], 10);
            long total = 0;
            boolean head = true;
            for (String part : parts) {
                if (head) {
                    head = false;
                    continue;
                }
                total += Long.parseLong(part, 10);
            }
            long diffIdle = idle - mLastIdle;
            long diffTotal = total - mLastTotal;
            mUsage = (int) ((float) (diffTotal - diffIdle) / diffTotal * 100);
            mLastTotal = total;
            mLastIdle = idle;
            //Log.i(TAG, "CPU total=" + total + "; idle=" + idle + "; usage=" + mUsage);
        }

    }


}
