package com.benzair.governancecore.assetsubdomain.presentationlayer;

import com.benzair.governancecore.assetsubdomain.businesslayer.AssetService;

import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

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

        //ENG-US: (||) is Shift + key right to the left of the enter key.
        if(assets == null || assets.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(assets);
    }

    @PostMapping()
    public AssetResponseModel addAsset(@RequestBody AssetRequestModel assetRequestModel){
        try{
            AssetResponseModel createdAsset = assetService.addAsset(assetRequestModel);
            return new ResponseEntity<>(createdAsset, HttpStatus.CREATED);
        } catch (InvalidInputException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
