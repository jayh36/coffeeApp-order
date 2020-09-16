
package coffeeApp.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@FeignClient(name="Product", url="${api.url.product}")
public interface PaymentService {

    @RequestMapping(method= RequestMethod.GET, path="/payments/check",produces = "application/json")
    @ResponseBody String checkPayment(@RequestParam("price") String price);

}