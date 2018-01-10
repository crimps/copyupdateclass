import com.crimps.ui.AuthUtils;
import com.crimps.ui.MainUI;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;

import static javafx.application.Platform.exit;

/**
 * @author crimps
 * @create 2018-01-09 16:33
 **/
public class Main {
    public static void main(String[] args) {
        try {
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!AuthUtils.authComputer()) {
            return;
        }else {
            new MainUI();
        }
    }
}
