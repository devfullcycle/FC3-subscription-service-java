package com.fullcycle.codeflix.subscription.domain.account.iam;

public interface IdentityGateway {

    UserId create(User anUser);

    void addUserToGroup(UserId userId, GroupId groupId);

    void removeUserFromGroup(UserId userId, GroupId groupId);
}
