package org.oseditor.graph;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FallOffHelperTest
{
    private static Logger log = LoggerFactory.getLogger(FallOffHelperTest.class);


    @Test
    public void testFallOff() throws Exception
    {
        FallOffHelper helper = new FallOffHelper();
        for (int i=0; i <= 11; i++)
        {
            log.info("{}: {}", i, helper.fallOff(new FallOffCurveDefinition(0.9, 1, 0.95, 0, 0.1, 10), i));
        }
    }
}
