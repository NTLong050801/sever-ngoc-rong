package com.girlkun.server;

import com.girlkun.server.Maintenance;
import com.girlkun.server.Manager;
import com.girlkun.services.Service;
import com.girlkun.services.func.Input;
import com.girlkun.utils.Logger;
import com.girlkun.network.io.Message;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;

public class Panel extends JPanel implements ActionListener {

    private JButton baotri, chatserver, kickplayer, thaydoiexp;

    public Panel() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);

        baotri = createButton("Bảo Trì Máy Chủ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(baotri, gbc);

       
        chatserver = createButton("Thông Báo Server");
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(chatserver, gbc);

        kickplayer = createButton("Đá All Player");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(kickplayer, gbc);

        thaydoiexp = createButton("Đổi Exp Server");
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(thaydoiexp, gbc);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.BLUE);
        button.setBackground(new Color(0, 255,255));
        button.setFocusPainted(false);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == baotri) {
            Maintenance.gI().start(5);
            Logger.error("Tiến Hành Bảo Trì !\n");
        } else if (e.getSource() == thaydoiexp) {
            String exp = JOptionPane.showInputDialog(this, "Bảng Exp Server\n"
                    + "Exp Server hiện tại: " + Manager.RATE_EXP_SERVER);
            if (exp != null) {
                Manager.RATE_EXP_SERVER = Byte.parseByte(exp);
                Logger.error("Exp hiện tại là: " + exp + "\n");
            }
        } else if (e.getSource() == chatserver) {
            String chat = JOptionPane.showInputDialog(this, "Thông Báo Server\n");
            if (chat != null) {
                Message msg = new Message(93);
                try {
                    msg.writer().writeUTF(chat);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
                }
                Service.gI().sendThongBaoOKAll("Hệ thống Thông báo: " + chat);
                msg.cleanup();
                Logger.error("Thông báo: " + chat + "\n");
            }
        } else if (e.getSource() == kickplayer) {
            new Thread(() -> {
                Client.gI().close();
            }).start();
        } 
    }
}
