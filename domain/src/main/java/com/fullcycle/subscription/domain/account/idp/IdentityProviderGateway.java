package com.fullcycle.subscription.domain.account.idp;

public interface IdentityProviderGateway {

    UserId create(User anUser);

    void addUserToGroup(UserId anId, GroupId aGroupId);

    void removeUserFromGroup(UserId anId, GroupId aGroupId);
}
