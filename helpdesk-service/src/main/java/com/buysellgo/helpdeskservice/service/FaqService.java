package com.buysellgo.helpdeskservice.service;

import com.buysellgo.helpdeskservice.repository.FaqRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FaqService {
    private final FaqRepository faqRepository;

}
