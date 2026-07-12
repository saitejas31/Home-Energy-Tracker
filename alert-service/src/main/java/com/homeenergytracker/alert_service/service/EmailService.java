import com.homeenergytracker.alert_service.repository.AlertRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final AlertRepository alertRepository;

    public void sendEmail(String to,String subject,String body,Long userId){
        log.info("Sending email to: {}, subject: {} ",to,subject);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("noreply@homeenergytracker.com");
        message.setSubject(subject);
        message.setText(body);

        try{
            javaMailSender.send(message);
            final Alert alertSent = Alert.builder()
                                        .userId(userId)
                                        .sent(true)
                                        .createdAt(LocalDateTime.now())
                                        .build();
            alertRepository.saveAndFlush(alertSent);
            log.info("Email sent successfully to: {}",to);
        }
        catch(MailException e){
            final Alert alertNotSent = Alert.builder()
                                            .userId(userId)
                                            .sent(false)
                                            .createdAt(LocalDateTime.now())
                                            .build();
            alertRepository.saveAndFlush(alertNotSent);
            log.error("Failed to send email to: {}",to,e);
            throw new RuntimeException("Failed to send email",e);
        }
    }
}
