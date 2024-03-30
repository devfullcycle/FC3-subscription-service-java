package com.fullcycle.codeflix.subscription.domain.account;

import com.fullcycle.codeflix.subscription.domain.person.Address;
import com.fullcycle.codeflix.subscription.domain.person.Document;
import com.fullcycle.codeflix.subscription.domain.person.Email;
import com.fullcycle.codeflix.subscription.domain.person.Name;

public sealed interface AccountCommand {

    record ChangeEmailCommand(Email anEmail) implements AccountCommand {
    }

    record ChangeNameCommand(Name aName) implements AccountCommand {
    }

    record ChangeDocumentCommand(Document aDocument) implements AccountCommand {
    }

    record ChangeBillingAddressCommand(Address anAddress) implements AccountCommand {
    }
}
