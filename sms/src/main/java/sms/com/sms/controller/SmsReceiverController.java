package sms.com.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import sms.com.sms.model.ReceiverDetails;
import sms.com.sms.service.OTPService;
import sms.com.sms.service.SmsReceiverServiceImpl;
import sms.com.sms.service.TwilioSMSService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/FireAlarm")
@AllArgsConstructor
@CrossOrigin("*")
public class SmsReceiverController {
@Autowired
    private SmsReceiverServiceImpl service;
   @Autowired
   private  TwilioSMSService twilioSMSService;

  @Autowired
    private final OTPService otpService;
   

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody ReceiverDetails user) {
        String response = service.sendOTP(user);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public List<ReceiverDetails> getAllReceiverDetails() {
        return service.getAllDetails();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody ReceiverDetails
 details) {
    if (details.getPhonenumber() == null || details.getPhonenumber().isEmpty()) {
        throw new IllegalArgumentException("Phone number is required");
    }
        // Check if phone number already exists in the system
         service.sendOTP(details);
        return ResponseEntity.ok("OTP sent successfully");
    }
   
    @PostMapping("/send-message-to-all-for-fireDetector")
    public String sendMessageToAllFofFireDetector() {
        String message = "Dear Subscriber,\n" + //
                        "\n" + //
                        "A GAS hazardhazard has been detected at [Your Funtain university/Eti-Osa]. "+"\n"+
                        "This is an emergency situation, and your immediate action is required"+"\n" 
                        +"Stay alert and take care, \"Location: [FOUNTAIN UNIVESITY/ ETI-OSA]\"+\"\\n" + //
                                                        " \"+\r\n" + //
                   "        \"Detection Time: [\"+ formattedDateTime +\"]\";\n" + //
                         "[Your Organization/ROBOTIC GROPE]";
        return twilioSMSService.sendDefaultMessageToAllUsers(message);
    }
    @PostMapping("/send-message-to-all-for-GasDetector")
    public String sendMessageToAllForGasDectector() {
        String message = "Dear Subscriber,\n" + //
                        "\n" + //
                        "A GAS hazard has been detected at [Your Funtain university/Eti-Osa]. "+"\n"+
                        "This is an emergency situation, and your immediate action is required"+"\n" 
                        +"Stay alert and take care, \"Location: [FOUNTAIN UNIVESITY/ ETI-OSA]\"+\"\\n" + //
                                                        " \"+\r\n" + //
                  "        \"Detection Time: [\"+ formattedDateTime +\"]\"\n" + //
                        
                         "[Your Organization/ROBOTIC GROPE]";
        return twilioSMSService.sendDefaultMessageToAllUsers(message);
    }
    // Step 2: Validate OTP and Save ReceiverDetails

    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestBody ReceiverDetails details) {
        if (details.getOtp() == null || details.getPhonenumber() == null) {
            return ResponseEntity.ok( "OTP and phone number are required");
            }
       else if (otpService.validateOtp(details.getPhonenumber(), details.getOtp())) {
            // If OTP is valid, save the details
         

                return ResponseEntity.ok("User registered successfully.");
            
           
        } else {
            return ResponseEntity.status(400).body("Invalid OTP.");
        }
       
    }

    

    /**
     * Retrieve receiver details by phone number.
     */
    @GetMapping("/{phonenumber}")
    public ResponseEntity<ReceiverDetails> getReceiverByPhoneNumber(@PathVariable String phonenumber) {
        ReceiverDetails details = service.getDetails(phonenumber);
        return ResponseEntity.ok(details);
    }

    /**
     * Update receiver details by phone number.
     */
    @PutMapping("/{phonenumber}/update")
    public ResponseEntity<ReceiverDetails> updateReceiverDetails(
            @PathVariable String phonenumber,
            @RequestParam String name) {
        ReceiverDetails updatedDetails = service.updateReceiversDetails(phonenumber, name);
        return ResponseEntity.ok(updatedDetails);
    }

    /**
     * Delete receiver details by phone number.
     */
    @DeleteMapping("/{phonenumber}")
    public ResponseEntity<String> deleteReceiver(@PathVariable String phonenumber) {
        service.deletes(phonenumber);
        return ResponseEntity.ok("Receiver deleted successfully.");
    }
}
