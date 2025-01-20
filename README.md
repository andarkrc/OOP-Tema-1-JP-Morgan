##### Grupa: 323 CA
##### Nume: Popa Andrei

## J. POO Morgan Chase & Co.: Proiect Etapa 1

1. ## Classes Overview
    1. ### Design
        The overall design of the project has remained pretty much the same: there are a lot of classes
        that act as functional containers. The biggest difference is now that there is no ActionHandler which implements every command.
        I used a modified version of the Command design pattern to create separate classes that would handle execution and logging for every single command.

    1. ### Bank
        The bank is the central entity of the program which contains the database and provides various functions to exchange data with it.

    1. ### CurrencyExchange
        This is the class that handles everything about currencies and the way to exchange from one into another.

    1. ### Database
        The database is the bank's storage which contains information about every user and their transactions.

    1. ### DatabaseEntry
        It's just an entry in the database: a storage unit for a single user. <br>
        The database entry also contains all the user's accounts and a transaction history.        
   
    1. ### User
        A class that contains information about a user.
   
    1. ### Commerciant
        A class that contains information about a commerciant.

    2. ### Account
        The account comes in 3 flavours: 
        * Classic - great for everyday spending
        * Savings - benefits from receiving interest every once in a while
        * Business - can have multiple associates that are using it
        
        The account also contains every card that was created for it.         

    1. ### Card
        The card also has 2 types:
        * Normal - can be used for online expenses
        * One Time - can also be used for online expenses, but is regenerated after 1 use
    
    2. ### Transactions
        The transaction family of classes are all specialised 'descendants' of DefaultTransaction.
        Every single transaction is designed to execute and handle the output for one input command.
        There also is a TransactionFactory that will create a new transaction based on the provided input.<br>
        The transaction also have 2 JsonObjects: `result` and `details`.
        * `result` stores details about a possible error related to the existence of whatever it operates on (user, account, card etc)
        * `details` stores the transaction's outcome after its execution

    1. ### JsonObject & JsonArray
        Custom implementations for writing to a Json file that I had made for the previous assignment and expanded it a little for the current assignment.

2. ## Designs Patterns
    * <strong>Singleton</strong>: the <strong>bank</strong> and <strong>currency exchange</strong> classes are singletons. 
    * <strong>Command</strong>: the transactions are implemented with a modified version of the command design pattern
    that uses a <strong>abstract class</strong> instead of an <strong>interface</strong> (as an abstract class is more flexible in my case).
    * <strong>Visitor</strong>: the printUsers and printTransactions are implemented using a <strong>JsonObjectVisitor</strong> (a visitor that returns the data as a JsonObject)
    * <strong>Factory</strong>: there is also a <strong>TransactionFactory</strong> class that creates new transactions based on a given CommandInput object

3. ## Afterthoughts
    This was a really interesting assignment, that forced me to create a flexible implementation, not just an implementation that works. I needed to make sure that it
    could be easily expandable in the second part of the project. I will experience first hand how easily I can modify and expand the functionality of the project in the second part.<br>
    My only complain about the assignment is that the output for the commands are really chaotic. Some commands have output in case of error(payOnline if the card doesn't exist) some have outputs only in print transactions,
    some don't even have any kind of output, which makes it harder to make a generalized system for output handling.

    ### AFTER PART 2
    This part was really, REALLY painful to do. I couldn't even finish it. I ran out of time and I just don't want to debug this anymore.
    I am almost going insane. The first part was already confusing enough, but this time, is just the cherry on top. I didn't think the assignment could be any more confusing
    than it already was, but here I am. Lucky me I had a great foundation that I laid back in part 1. Even so, I am still contemplating my life choices.
    I'd really love to see the 'official' source that was used to generate the refs.
    </br>
    The assignment had a lot of potential, but the execution was disastrous, stripping away my freedom to do it in a coherent way (because nor the tasks or refs were in any way coherent themselves). 