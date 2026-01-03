package com.jrlcst.couponapi.coupon.infrastructure.rest;

import com.jrlcst.couponapi.coupon.domain.model.Coupon;
import com.jrlcst.couponapi.coupon.domain.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CouponV1ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponRepository repository;

    @Test
    @DisplayName("V1 Scenario 1 — Create coupon successfully")
    void createCouponSuccessfully() throws Exception {
        final String body = """
            {
              "code": "ABC-123",
              "description": "Iure saepe amet.",
              "discountValue": 0.8,
              "expirationDate": "2026-01-05T17:14:45.180Z",
              "published": false
            }
            """;

        mockMvc.perform(post("/v1/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.description").value("Iure saepe amet."))
                .andExpect(jsonPath("$.discountValue").value(0.8))
                .andExpect(jsonPath("$.expirationDate").value(startsWith("2026-01-05T17:14:45.18")))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.published").value(false))
                .andExpect(jsonPath("$.redeemed").value(false))
                .andExpect(jsonPath("$.message").doesNotExist()); // Sem wrapper
    }

    @Test
    @DisplayName("V1 Scenario 6 — Get coupon by id successfully")
    void getCouponByIdSuccessfully() throws Exception {
        UUID id = createCoupon("GETV1A");

        mockMvc.perform(get("/v1/coupon/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.code").value("GETV1A"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.message").doesNotExist());
    }

    @Test
    @DisplayName("V1 Scenario 8 — Delete coupon successfully")
    void deleteCouponSuccessfully() throws Exception {
        UUID id = createCoupon("DELV1A");

        mockMvc.perform(delete("/v1/coupon/" + id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("V1 Scenario 2 — Create coupon with expiration date in the past")
    void createCouponWithPastExpirationDate() throws Exception {
        final String body = """
            {
              "code": "ABC123",
              "description": "Expired coupon",
              "discountValue": 1.0,
              "expirationDate": "2024-01-01T10:00:00Z",
              "published": false
            }
            """;

        mockMvc.perform(post("/v1/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The expiration date cannot be in the past"))
                .andExpect(jsonPath("$.requestId").exists());
    }

    @Test
    @DisplayName("V1 Scenario 3 — Create coupon with discount lower than minimum")
    void scenario3() throws Exception {
        final String body = """
            {
              "code": "ABC123",
              "description": "Invalid discount",
              "discountValue": 0.2,
              "expirationDate": "2026-01-05T17:14:45Z",
              "published": true
            }
            """;

        mockMvc.perform(post("/v1/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The discount value must be at least 0.5"));
    }

    @Test
    @DisplayName("V1 Scenario 4 — Create coupon with invalid code after sanitization")
    void scenario4() throws Exception {
        final String body = """
            {
              "code": "A-1!",
              "description": "Invalid code",
              "discountValue": 1.0,
              "expirationDate": "2026-01-05T17:14:45Z",
              "published": false
            }
            """;

        mockMvc.perform(post("/v1/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The coupon code must contain exactly 6 alphanumeric characters"));
    }

    @Test
    @DisplayName("V1 Scenario 5 — Create coupon with missing required fields")
    void scenario5() throws Exception {
        final String body = """
            {
              "code": "",
              "discountValue": 1.0
            }
            """;

        mockMvc.perform(post("/v1/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("An error occurred while processing your request"));
    }

    @Test
    @DisplayName("V1 Scenario 7 — Get coupon that does not exist")
    void scenario7() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/v1/coupon/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("V1 Scenario 9 — Delete coupon already deleted")
    void scenario9() throws Exception {
        UUID id = createCoupon("DELTW1");
        
        // First delete
        mockMvc.perform(delete("/v1/coupon/" + id)).andExpect(status().isNoContent());

        // Second delete
        mockMvc.perform(delete("/v1/coupon/" + id))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message").value("Coupon already deleted"));
    }

    @Test
    @DisplayName("V1 Scenario 10 — Delete coupon that does not exist")
    void scenario10() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/v1/coupon/" + id))
                .andExpect(status().isNotFound());
    }

    private UUID createCoupon(final String code) {
        final Coupon coupon = Coupon.createNew(
                code,
                "Iure saepe amet.",
                new BigDecimal("0.8"),
                LocalDateTime.parse("2026-01-05T17:14:45.180"),
                false,
                LocalDateTime.now()
        );
        return repository.save(coupon).getId();
    }
}
