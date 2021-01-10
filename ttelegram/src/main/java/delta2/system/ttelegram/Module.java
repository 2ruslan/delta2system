package delta2.system.ttelegram;

import android.Manifest;
import android.content.Context;

import java.util.ArrayList;

import delta2.system.framework.abstraction.ModuleBase;
import delta2.system.framework.common.MessageFactory;
import delta2.system.framework.interfaces.IBus;
import delta2.system.framework.interfaces.ICommandManager;

import delta2.system.framework.interfaces.IMessage;
import delta2.system.framework.interfaces.IMessageSend;
import delta2.system.ttelegram.transporttelegram.SettingsActivity;
import delta2.system.ttelegram.transporttelegram.preferences.PreferencesHelper;
import delta2.system.ttelegram.transporttelegram.transport.TelegramTransport;

public class Module extends ModuleBase {

    public static final String _MODULE_CODE = "ttm";

    TelegramTransport transport;

    public Module(Context c, IBus b) {
        super(c, b);
        PreferencesHelper.Init(c);
    }

    @Override
    public String GetModuleId() {
        return "4568e9c7-f547-41c2-9cd0-8187a4aa32b7";
    }

    @Override
    public String GetModuleName() {
        return _MODULE_CODE;
    }

    @Override
    public String GetDescription() {
        return context.getResources().getString(R.string.ttm_module_name);
    }

    @Override
    public ArrayList<String> GetPermissions() {
        return new ArrayList<String>(){{
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }};
    }

    @Override
    protected ICommandManager GetModuleCommandManager() {
        return null;
    }

    @Override
    protected Class<?> GetSettingsClass() {
        return SettingsActivity.class;
    }

    @Override
    public void Begin() {
        try {
            transport = new TelegramTransport(context, this);
            transport.connect();

            transport.SendMessage(MessageFactory.GetMessageSendText("ok 123 123 123"));
        }
        catch (Exception ex){
            logger.error(ex);
        }
    }

    @Override
    public void Finish() {
        if (transport != null)
            transport.destroy();
    }

    @Override
    protected void SendMessage(IMessage msg){
        if (msg instanceof IMessageSend)
            transport.SendMessage((IMessageSend) msg);
    }
}
