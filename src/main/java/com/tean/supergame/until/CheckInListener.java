package com.tean.supergame.until;

import com.tean.supergame.model.entity.PointTransaction;
import com.tean.supergame.model.entity.UserModel;
import com.tean.supergame.repository.PointTransactionRepository;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class CheckInListener implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @PostUpdate
    public void onPostPersist(UserModel userModel) {
        PointTransactionRepository repository = context.getBean(PointTransactionRepository.class);

        PointTransaction pointTransaction = new PointTransaction();

        pointTransaction.setUserId(userModel.getId());
        pointTransaction.setAmount(10);
        pointTransaction.setBalance(userModel.getPoint());
        repository.save(pointTransaction);
    }
}