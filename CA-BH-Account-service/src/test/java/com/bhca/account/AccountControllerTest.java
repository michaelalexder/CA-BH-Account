package com.bhca.account;

import com.bhca.account.service.TransactionApiClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.AccountData;
import org.openapitools.client.model.CreateAccountRequest;
import org.openapitools.client.model.TransactionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionApiClient apiClient;

    @Autowired
    private ObjectMapper objectMapper;

    private static class Api {

        private static final String CREATE = "/api/v1/account";
        private static final String ACCOUNT_DATA = "/api/v1/account/{customerId}";
    }

    @Test
    public void testZeroCreditAccount() throws Exception {
        UUID customerId = UUID.randomUUID();
        this.mockMvc.perform(post(Api.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new CreateAccountRequest().customerId(customerId).initialCredit(BigDecimal.ZERO)))
        ).andExpect(status().isOk());
        String response = this.mockMvc.perform(get(Api.ACCOUNT_DATA, customerId)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<AccountData> result = objectMapper.readValue(response, new TypeReference<List<AccountData>>() {
        });
        assertEquals(result.size(), 1);
        assertEquals(0, result.get(0).getBalance().compareTo(BigDecimal.ZERO));
        assertNotNull(result.get(0).getNumber());
        assertEquals(result.get(0).getTransactions().size(), 0);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPositiveCreditAccounts() throws Exception {
        UUID customerId = UUID.randomUUID();
        BigDecimal credit = BigDecimal.TEN;
        when(apiClient.transactions(any())).thenAnswer(invocation -> {
            List<UUID> args = (List<UUID>) invocation.getArguments()[0];
            return Collections.singletonList(
                    new TransactionItem().account(args.get(0))
                            .createdAt(OffsetDateTime.now()).amount(credit));
        });
        this.mockMvc.perform(post(Api.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new CreateAccountRequest().customerId(customerId).initialCredit(credit)))
        ).andExpect(status().isOk());
        String response = this.mockMvc.perform(get(Api.ACCOUNT_DATA, customerId)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<AccountData> result = objectMapper.readValue(response, new TypeReference<List<AccountData>>() {
        });
        assertEquals(result.size(), 1);
        assertEquals(0, result.get(0).getBalance().compareTo(credit));
        assertNotNull(result.get(0).getNumber());
        assertEquals(result.get(0).getTransactions().size(), 1);
    }

    @Test
    public void testNegativeCredit() throws Exception {
        this.mockMvc.perform(post(Api.CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new CreateAccountRequest().customerId(UUID.randomUUID()).initialCredit(new BigDecimal(-10))))
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void testUnknownAccountData() throws Exception {
        this.mockMvc.perform(get(Api.ACCOUNT_DATA, UUID.randomUUID())).andExpect(status().isOk());
    }
}
