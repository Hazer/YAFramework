package io.vithor.yamvpframework.migration;

/**
 * Created by Vithorio Polten on 2/23/16.
 */
public final class SampleVersionManager extends AbstractVersionManager {
    /**
     * DO NOT SUBCLASS THIS, SAMPLE ONLY CLASS
     */
    private static SampleVersionManager instance = new SampleVersionManager();

    public static SampleVersionManager getInstance() {
        return instance;
    }

    private SampleVersionManager() {
        super();
        addMigrations(
                new V1_TestMigration()
        );
    }
}