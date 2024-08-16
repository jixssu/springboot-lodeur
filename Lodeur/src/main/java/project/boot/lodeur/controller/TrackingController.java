package project.boot.lodeur.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TrackingController {
	@GetMapping("/track")
	public String insert() {
		return "./delivery/trackingForm";
	}
}
