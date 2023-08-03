package io.javabrains.inbox.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import io.javabrains.inbox.email.EmailService;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.FolderService;

@Controller
public class ComposeController {

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FolderService folderService;
    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/compose")
    public String getComposePage(
            @RequestParam(required = false) String to,
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
        model.addAttribute("stats", folderService.mapCountToLabels(userId));
        List<String> uniqueToIds = splitIds(to);
        model.addAttribute("toIds", String.join(", ", uniqueToIds));

        return "compose-page";
    }

    private List<String> splitIds(String to) {
        if (!StringUtils.hasText(to)) {
            return new ArrayList<String>();
        }
        String[] splitIds = to.split(",");
        List<String> uniqueToIds = Arrays.asList(splitIds)
                .stream()
                .map(id -> StringUtils.trimWhitespace(id))
                .filter(id -> StringUtils.hasText(id))
                .distinct()
                .collect(Collectors.toList());
        return uniqueToIds;
    }

    @PostMapping("/sendEmail")
    public ModelAndView sendEmail(
            @RequestBody MultiValueMap<String, String> formData,
            @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return new ModelAndView("redirect:/");
        }
        String from = principal.getAttribute("login");
        List<String> toIds = splitIds(formData.getFirst("toIds"));
        String subject = formData.getFirst("subject");
        String body = formData.getFirst("body");
        emailService.sendEmail(from, toIds, subject, body);
        return new ModelAndView("redirect:/");
    }
}
