package io.javabrains.inbox.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.javabrains.inbox.email.Email;
import io.javabrains.inbox.email.EmailRepository;
import io.javabrains.inbox.emaillist.EmailListItem;
import io.javabrains.inbox.emaillist.EmailListItemKey;
import io.javabrains.inbox.emaillist.EmailListItemRepository;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.FolderService;
import io.javabrains.inbox.folders.UnreadEmailStatsRepository;

@Controller
public class EmailViewController {

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FolderService folderService;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailListItemRepository emailListItemRepository;
    @Autowired
    private UnreadEmailStatsRepository unreadEmailStatsRepository;

    @GetMapping(value = "/emails/{id}")
    public String emailView(
            @RequestParam String folder,
            @PathVariable UUID id,
            @AuthenticationPrincipal OAuth2User principal,
            Model model) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }

        // Fetch folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        List<Folder> defaultFolders = folderService.fetchDefualtFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        Optional<Email> optionalEmail = emailRepository.findById(id);
        if (!optionalEmail.isPresent()) {
            return "inbox-pages";
        }

        Email email = optionalEmail.get();
        String toIds = String.join(",", email.getTo());
        model.addAttribute("email", email);
        model.addAttribute("toIds", toIds);

        EmailListItemKey key = new EmailListItemKey();
        key.setId(userId);
        key.setLabel(folder);
        key.setTimeUUID(email.getId());

        Optional<EmailListItem> optionEmailLIstItem = emailListItemRepository.findById(key);
        if (optionEmailLIstItem.isPresent()) {
            EmailListItem emailListItem = optionEmailLIstItem.get();
            if (emailListItem.isUnread()) {
                emailListItem.setUnread(false);
                emailListItemRepository.save(emailListItem);
                unreadEmailStatsRepository.decreamentUnreadCount(userId, folder);
            }
        }
        model.addAttribute("stats", folderService.mapCountToLabels(userId));

        return "email-page";
    }

}
