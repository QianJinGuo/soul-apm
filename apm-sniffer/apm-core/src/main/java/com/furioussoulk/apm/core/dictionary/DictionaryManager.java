package com.furioussoulk.apm.core.dictionary;

public class DictionaryManager {
    /**
     * @return {@link ApplicationDictionary} to find application id for application code and network address.
     */
    public static ApplicationDictionary findApplicationCodeSection() {
        return ApplicationDictionary.INSTANCE;
    }

    /**
     * @return {@link OperationNameDictionary} to find service id.
     */
    public static OperationNameDictionary findOperationNameCodeSection() {
        return OperationNameDictionary.INSTANCE;
    }
}
