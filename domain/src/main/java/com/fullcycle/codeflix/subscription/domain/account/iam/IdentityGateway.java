package com.fullcycle.codeflix.subscription.domain.account.iam;

import java.util.List;

public interface IdentityGateway {

    IAMUser create(IAMUser anUser);

    List<IAMUser> allUsers(UsersQuery aQuery);

    void addUserToGroup(IAMUserId userId, GroupId groupId);

    void removeUserFromGroup(IAMUserId userId, GroupId groupId);
}
