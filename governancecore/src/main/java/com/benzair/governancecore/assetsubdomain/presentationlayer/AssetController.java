package com.benzair.governancecore.assetsubdomain.presentationlayer;

import com.benzair.governancecore.assetsubdomain.businesslayer.AssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {
    private final AssetService assetService;

    public AssetController(AssetService assetService){
        this.assetService = assetService;
    }

    @GetMapping()
    public ResponseEntity<List<AssetResponseModel>> getAllAssets(){
        
        List<AssetResponseModel> assets = assetService.getAllAssets();
        return ResponseEntity.ok(assets);
    }

    @PostMapping()
    public ResponseEntity<AssetResponseModel> addAsset(@Valid @RequestBody AssetRequestModel assetRequestModel){
            AssetResponseModel createdAsset = assetService.addAsset(assetRequestModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAsset);
    }
}
