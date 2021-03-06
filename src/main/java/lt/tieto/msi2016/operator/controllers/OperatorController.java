package lt.tieto.msi2016.operator.controllers;

import lt.tieto.msi2016.auth.services.UserService;
import lt.tieto.msi2016.missions.services.MissionService;
import lt.tieto.msi2016.operator.model.Operator;
import lt.tieto.msi2016.operator.services.OperatorService;
import lt.tieto.msi2016.utils.controller.BaseController;
import lt.tieto.msi2016.utils.services.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static lt.tieto.msi2016.utils.constants.Roles.OPERATOR;

/**
 * Created by localadmin on 16.8.8.
 */
@RestController
public class OperatorController extends BaseController {

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityHolder securityHolder;

    @Autowired
    private MissionService missionService;



    @Secured(OPERATOR)
    @RequestMapping(value = "/api/users/{id}/operatorState", method = RequestMethod.GET)
    public ResponseEntity<Operator> getOperator(@PathVariable Long id){

        if(canAccessInfo(id)) {
            Operator operator = operatorService.getOperatorState(id);
            return operator != null ? ResponseEntity.ok(operator) : ResponseEntity.ok(new Operator());
        } else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }



    @Secured(OPERATOR)
    @RequestMapping(value = "/api/users/{id}/operatorState", method = RequestMethod.POST)
    public ResponseEntity<Operator> generateToken(@PathVariable Long id){
      if(canAccessInfo(id)){
         return ResponseEntity.ok(operatorService.generateId(id));
      }

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    private boolean canAccessInfo(Long id){
        return securityHolder.getUserPrincipal().getUsername().equals(userService.getUserInfo(id).getUserName());
    }



}
