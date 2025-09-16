package com.cashbox.AuthService.controller;


import com.cashbox.AuthService.common.BaseApiResponse;
import com.cashbox.AuthService.dto.request.PinChangeRequest;
import com.cashbox.AuthService.dto.request.PinCreateRequest;
import com.cashbox.AuthService.dto.request.PinVerificationRequest;
import com.cashbox.AuthService.dto.response.PinCreateResponse;
import com.cashbox.AuthService.dto.response.PinVerificationResponse;
import com.cashbox.AuthService.service.PinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pin")
@RequiredArgsConstructor
@Tag(name = "PIN Management", description = "Endpoints for creating, changing and verifying user wallet PINs")
public class PinController {

    private final PinService pinService;

    @Operation(summary = "Create/Set PIN",
            description = "Create or reset a wallet PIN for a user")
    @ApiResponse(responseCode = "200", description = "PIN set successfully")
    @PostMapping("/create")
    public BaseApiResponse<PinCreateResponse> createPin(@Valid @RequestBody PinCreateRequest request) {
        PinCreateResponse response = pinService.setPin(request.getUserId(), request.getPin());
        return BaseApiResponse.success("PIN set successfully", response);
    }

    @Operation(summary = "Change PIN",
            description = "Change an existing wallet PIN for a user")
    @ApiResponse(responseCode = "200", description = "PIN changed successfully")
    @PostMapping("/change")
    public BaseApiResponse<Void> changePin(@Valid @RequestBody PinChangeRequest request) {
        pinService.changePin(request.getUserId(), request.getOldPin(), request.getNewPin());
        return BaseApiResponse.success("PIN changed successfully");
    }

    @Operation(summary = "Verify PIN",
            description = "Verify a wallet PIN for a user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Verification result",
                            content = @Content(schema = @Schema(implementation = PinVerificationResponse.class)))
            })
    @PostMapping("/verify")
    public BaseApiResponse<PinVerificationResponse> verifyPin(
            @Valid @RequestBody PinVerificationRequest request) {
        boolean isValid = pinService.verifyPin(request.getPin());
        return BaseApiResponse.success(
                "PIN verification result",
                new PinVerificationResponse(isValid)
        );
    }
}

