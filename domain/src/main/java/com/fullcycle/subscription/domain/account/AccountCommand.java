package com.fullcycle.subscription.domain.account;

import com.fullcycle.subscription.domain.person.Address;
import com.fullcycle.subscription.domain.person.Document;
import com.fullcycle.subscription.domain.person.Email;
import com.fullcycle.subscription.domain.person.Name;

public sealed interface AccountCommand {

    record ChangeProfileCommand(Name aName, Address aBillingAddress) implements AccountCommand {

    }

    record ChangeEmailCommand(Email anEmail) implements AccountCommand {

    }

    record ChangeDocumentCommand(Document aDocument) implements AccountCommand {

    }
}
