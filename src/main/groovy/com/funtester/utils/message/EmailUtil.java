package com.funtester.utils.message;

import com.funtester.frame.SourceCode;

/**
 * 发送邮件的类，暂时使用静态方法，默认使用QQ邮箱发送
 */
public class EmailUtil extends SourceCode {

//    private static Logger logger = LogManager.getLogger(EmailUtil.class);
//
//    private static Session session;
//
//    private static void instance() {
//        Security.addProvider(new Provider());
//        //设置邮件会话参数
//        Properties props = new Properties();
//        //邮箱的发送服务器地址
//        props.setProperty("mail.smtp.host", EmailConstant.QQ_HOST);
//        props.setProperty("mail.smtp.socketFactory.class", EmailConstant.SSL_FACTORY);
//        props.setProperty("mail.smtp.socketFactory.fallback", "false");
//        //邮箱发送服务器端口,这里设置为465端口
//        props.setProperty("mail.smtp.port", "465");
//        props.setProperty("mail.smtp.socketFactory.port", "465");
//        props.put("mail.smtp.auth", "true");
//        //获取到邮箱会话,利用匿名内部类的方式,将发送者邮箱用户名和密码授权给jvm
//        session = Session.getDefaultInstance(props, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(EmailConstant.QQ_USERNAME, EmailConstant.QQ_PASSWORD);
//            }
//        });
//    }
//
//    /**
//     * 向邮箱发送邮件
//     *
//     * @param email   对方的邮件地址
//     * @param title   邮件的标题
//     * @param content 邮件的内容
//     * @return
//     */
//    public static boolean sendEmail(String email, String title, String content) {
//        //多线程优化
//        if (session == null) {
//            synchronized (EmailUtil.class) {
//                if (session == null)
//                    instance();
//            }
//        }
//        try {
//            Message msg = new MimeMessage(session);
//            //设置发件人
//            msg.setFrom(new InternetAddress(EmailConstant.QQ_USERNAME));
//            //设置收件人,to为收件人,cc为抄送,bcc为密送
//            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
//            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(email, false));
//            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(email, false));
//            msg.setSubject(title);
//            //设置邮件消息
//            msg.setText(content);
//            //设置发送的日期
//            msg.setSentDate(new Date());
//            //调用Transport的send方法去发送邮件
//            Transport.send(msg);
//            return true;
//        } catch (MessagingException e) {
//            logger.error(e.getMessage());
//            return false;
//        }
//    }
}
