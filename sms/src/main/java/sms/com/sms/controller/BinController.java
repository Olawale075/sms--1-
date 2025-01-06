package sms.com.sms.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import sms.com.sms.model.BinUser;

import sms.com.sms.service.OTPService;
import sms.com.sms.service.BinSevice;

import sms.com.sms.service.TwilioSMSService;

@RestController
@RequestMapping("api/v1/BinAlarm")

@CrossOrigin("*")
public class BinController {

    @Autowired
    private BinSevice service;
    @Autowired
    private TwilioSMSService twilioSMSService;

    @Autowired
    private  OTPService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody BinUser user) {
        String response = service.sendOTP(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<BinUser> getAllReceiverDetails() {
        return service.getAllDetails();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody BinUser details) {
        // Check if phone number already exists in the system
        String response = service.sendOTP(details);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-message-to-all-for-BinFull")
    public String sendMessageToAllFofFireDetector() { 
         LocalDateTime now = LocalDateTime.now();

        // Format the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        String message = "We have detected that your bin is full and requires attention."+"\n"+
        "Location: [FOUNTAIN UNIVESITY/ ETI-OSA]"+"\n "+
        "Detection Time: ["+ formattedDateTime +"]";
        return twilioSMSService.sendDefaultMessageToAllUsers(message);
    }
  
    // Step 2: Validate OTP and Save BinUser

    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestParam String phonenumber, @RequestParam String otp) {
        if (otpService.validateOtp(phonenumber, otp)) {
            // If OTP is valid, save the details

            BinUser details = service.findTempUser(phonenumber);
            if (details != null) {
                service.saveUser(details);
                return ResponseEntity.ok("User registered successfully.");
            }
            return ResponseEntity.status(404).body("User data not found.");
        } else {
            return ResponseEntity.status(400).body("Invalid OTP.");
        }
    }

    /**
     * Send an OTP to a specific phone number.
     */
    // @PostMapping("/send-otp")
    // public String sendOTP(@RequestParam String phonenumber) {

    // return service.sendOTP(phonenumber);
    // }

    /**
     * Retrieve receiver details by phone number.
     */
    @GetMapping("/{phonenumber}")
    public ResponseEntity<BinUser> getReceiverByPhoneNumber(@PathVariable String phonenumber) {
        BinUser details = service.getDetails(phonenumber);
        return ResponseEntity.ok(details);
    }

    /**
     * Update receiver details by phone number.
     */
    @PutMapping("/{phonenumber}/update")
    public ResponseEntity<BinUser> updateReceiverDetails(
            @PathVariable String phonenumber,
            @RequestParam String name) {
        BinUser updatedDetails = service.updateReceiversDetails(phonenumber, name);
        return ResponseEntity.ok(updatedDetails);
    }

    /**
     * Delete receiver details by phone number.
     */
    @DeleteMapping("/{phonenumber}")
    public ResponseEntity<String> deleteReceiver(@PathVariable String phonenumber) {
        service.delete(phonenumber);
        return ResponseEntity.ok("Receiver deleted successfully.");
    }
}
