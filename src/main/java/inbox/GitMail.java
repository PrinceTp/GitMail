package inbox;

import java.nio.file.Path;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import inbox.email.EmailService;
import inbox.folders.Folder;
import inbox.folders.FolderRepository;

@SpringBootApplication
@RestController
public class GitMail {

	@Autowired
	FolderRepository folderRepository;
	@Autowired
	EmailService emailService;

	public static void main(String[] args) {
		SpringApplication.run(GitMail.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void init() {
		folderRepository.save(new Folder("PrinceTp", "Work", "blue"));
		folderRepository.save(new Folder("PrinceTp", "Home", "green"));
		folderRepository.save(new Folder("PrinceTp", "Uni", "yellow"));

		for (int i = 0; i < 10; i++) {
			emailService.sendEmail("PrinceTp", Arrays.asList("PrinceTp"),
					"Title : " + RandomStringUtils.randomAlphabetic(10),
					RandomStringUtils.randomAlphabetic(1000));
		}

		emailService.sendEmail("abc", Arrays.asList("def", "abc"), "Hellow ", "body");
	}
}
