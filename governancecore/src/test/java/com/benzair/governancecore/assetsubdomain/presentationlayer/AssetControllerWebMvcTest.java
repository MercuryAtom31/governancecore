package com.benzair.governancecore.assetsubdomain.presentationlayer;

import com.benzair.governancecore.assetsubdomain.businesslayer.AssetService;
import com.benzair.governancecore.assetsubdomain.datalayer.AssetType;
import com.benzair.governancecore.assetsubdomain.datalayer.DataClassification;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssetController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AssetControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssetService assetService;

    @Test
    void getAllAssets_shouldReturnAllAssets() throws Exception {

        // ARRANGE
        List<AssetResponseModel> assets = List.of(
                AssetResponseModel.builder()
                .firstName("Server")
                .lastName("1")
                        .owner("IT department")
                        .assetType(AssetType.SERVER)
                        .classification(DataClassification.CONFIDENTIAL)
                        .description("Handles customer data")
                        .build()
        );

        when(assetService.getAllAssets()).thenReturn(assets);

        // ACT
        mockMvc.perform(get("/api/v1/assets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Server"))
                .andExpect(jsonPath("$[0].lastName").value("1"))
                .andExpect(jsonPath("$[0].owner").value("IT department"));

        // ASSERT
        verify(assetService).getAllAssets();
        verifyNoMoreInteractions(assetService);
    }
}
