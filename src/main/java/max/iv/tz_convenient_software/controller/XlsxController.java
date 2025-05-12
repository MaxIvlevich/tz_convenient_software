package max.iv.tz_convenient_software.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import max.iv.tz_convenient_software.services.XlsxServiceInterface;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/xlsx")
@RequiredArgsConstructor
@Tag(name = "XLSX API", description = "Finding the nth minimum number in a file")
public class XlsxController {

    private final XlsxServiceInterface xlsxService;

    @Operation(summary = "Get the nth minimum number from an xlsx file")
    @GetMapping("/nth-min")
    public Integer getNthMin(
            @RequestParam String path,
            @RequestParam int n
    ) {
        return xlsxService.findNthMin(path, n);
    }
}
