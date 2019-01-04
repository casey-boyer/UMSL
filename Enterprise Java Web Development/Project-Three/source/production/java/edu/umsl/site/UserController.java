package edu.umsl.site;

import edu.umsl.config.annotation.WebController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.Map;

@WebController
public class UserController {
    @Inject UserManager userManager;

    @RequestMapping(value = "user/list", method = RequestMethod.GET)
    public String list(Map<String, Object> model)
    {
        model.put("accounts", this.userManager.getAllUsers());
        return "user/list";
    }

}
