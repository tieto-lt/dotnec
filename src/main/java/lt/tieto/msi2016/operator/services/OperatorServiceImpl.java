package lt.tieto.msi2016.operator.services;

import lt.tieto.msi2016.operator.model.Operator;
import lt.tieto.msi2016.operator.repository.OperatorRepository;
import lt.tieto.msi2016.operator.repository.model.OperatorDb;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by localadmin on 16.8.8.
 */
@Service
public class OperatorServiceImpl implements OperatorService {

    @Resource
    private OperatorRepository operatorRepository;


    @Transactional(readOnly = true)
    @Override
    public Operator getOperatorState(Long id)
    {
        return fillOperator(operatorRepository.findById(id));
    }


    @Transactional
    private Operator fillOperator(OperatorDb operatorDb){
        Operator operator = new Operator();
        if(operatorDb == null){
            return operator;
        } else {
            return operator = Operator.valueOf(operatorDb);
        }
    }

    public Operator generateId(Long id){
        OperatorDb opDb = operatorRepository.findById(id);
        if(opDb == null) {
            opDb = new OperatorDb();
            opDb.setToken(UUID.randomUUID().toString());
            opDb.setVerified(false);
            opDb.setUserId(id);
            operatorRepository.create(opDb);
        }else {
            opDb.setToken(UUID.randomUUID().toString());
            opDb.setVerified(false);
            operatorRepository.update(opDb);
        }
        return Operator.valueOf(opDb);
    }

    @Transactional
    public void verifyOperatorService(String token)
    {
        OperatorDb operatorDb = operatorRepository.findByToken(token);
        if (operatorDb != null) {
            operatorRepository.changeOperatorVerify(operatorDb.getUserId(), true);
        }
    }

    @Transactional
    public boolean tokenExists(String operatorToken){
        return operatorRepository.findByToken(operatorToken) != null;
    }

    @Transactional(readOnly = true)
    public boolean isVerified(String operatorToken) {
        return operatorRepository.isVerified(operatorToken);
    }


}
