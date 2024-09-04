package Banking.Application.Controller;
import Banking.Application.Dto.*;
import Banking.Application.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management Apis" )
public class UserController {
    @Autowired
    UserService userService;

    @Operation(
            summary = "Creating new User Accounts",
            description="Creating new User with bank account"
    )
    @ApiResponse(
            responseCode = "100",
            description=" Http status 100 created"
    )

    @PostMapping()
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

    @Operation(
            summary = "User Account Balance Enquiry",
            description="Enquiring bank account balance"
    )
    @ApiResponse(
            responseCode = "101",
            description=" Http status 101 created"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest)
    {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @Operation(
            summary = "User Name Enquiry",
            description="Enquiring a User Name"
    )
    @ApiResponse(
            responseCode = "102",
            description=" Http status 102 created"
    )


    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest)
    {
        return userService.nameEnquiry(enquiryRequest);
    }

    @Operation(
            summary = "Crediting an Account",
            description="User Credit Request"
    )
    @ApiResponse(
            responseCode = "103",
            description=" Http status 103 created"
    )
@PostMapping("/credit")
    public  BankResponse creditAccount (@RequestBody CreditDebitRequest creditDebitRequest)
{
    return userService.creditAccount(creditDebitRequest);
}    @Operation(
            summary = "Debiting an Account",
            description="User Debit Request"
    )
    @ApiResponse(
            responseCode = "104",
            description=" Http status 104 created"
    )
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){

        return  userService.debitAccount(creditDebitRequest);
}    @Operation(
            summary = "Account Money Transfer",
            description="Transfering Money to a different User Account"
    )
    @ApiResponse(
            responseCode = "105",
            description=" Http status 105 created"
    )

    @PostMapping("/transfer")
    public  BankResponse transfer(@RequestBody TransferRequest transferRequest)
{
    return  userService.transfer(transferRequest);
}
@PostMapping("/login")
    public  BankResponse login( @RequestBody LoginDto loginDto)
{
    return  userService.login(loginDto);
}

}


