package com.fullcycle.subscription.application.account;

import com.fullcycle.subscription.application.UseCase;
import com.fullcycle.subscription.domain.account.AccountId;

public abstract class UpdateBillingInfo extends UseCase<UpdateBillingInfo.Input, UpdateBillingInfo.Output> {

    public interface Input {
        String accountId();
        String zipcode();
        String number();
        String complement();
        String country();
    }

    public interface Output {
        AccountId accountId();
    }
}
