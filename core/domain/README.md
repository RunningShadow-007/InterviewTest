## Domain Layer

> In Clean Architecture, a use case should represent a clear business operation.

1. #### Separation of Concerns : Separates business logic from UI and data access
3. #### Improved Testability : Facilitates unit testing
4. #### Combine the data from various Repositories, perform some logical processing according to business requirements, and finally return the mixed data needed by the UI layer.
5. #### For example, the GetCompositeBizDataUseCase receives the DeFiDataRepository, WalletDataRepository, and SettingDataRepository, and then, after carrying out various business logic processing, returns the processed data required by the UI.
