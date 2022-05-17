package com.university.platform.service;

import com.university.platform.model.Attachment;
import com.university.platform.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public void saveAttachments(Set<Attachment> attachment) {
        attachmentRepository.saveAll(attachment);
    }
}
