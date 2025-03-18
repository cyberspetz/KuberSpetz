package com.swami.transactionengine.idvmicroservice;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/idv")
public class IdvController {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public IdvController(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @PostMapping("/verify")
  public ResponseEntity<String> verifyIdentity(@RequestParam String id) {
    kafkaTemplate.send("idv-topic", "IDV complete for: " + id);
    return ResponseEntity.ok("Verification done for: " + id);
  }
}
