package com.benzair.governancecore.assetsubdomain.businesslayer;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.benzair.governancecore.assetsubdomain.datalayer.AssetRepository;
import com.benzair.governancecore.assetsubdomain.datamapperlayer.AssetRequestMapper;
import com.benzair.governancecore.assetsubdomain.datamapperlayer.AssetResponseMapper;

@ExtendWith(MockitoExtension.class) // JUnit 5 annotation to enable Mockito support in this test class
public class AssetServiceImplUnitTest {
    
    @Mock
    AssetRepository assetRepository;

    @Mock
    AssetRequestMapper assetRequestMapper;

    @Mock
    AssetResponseMapper assetResponseMapper;

    @InjectMocks
    AssetServiceImpl assetService;
}
