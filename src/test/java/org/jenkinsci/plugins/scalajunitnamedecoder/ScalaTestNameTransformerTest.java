package org.jenkinsci.plugins.scalajunitnamedecoder;

import static org.junit.Assert.*;

import org.junit.Test;

public class ScalaTestNameTransformerTest {
    private final ScalaTestNameTransformer transformer = new ScalaTestNameTransformer();
    
    @Test public void single_name_doesnt_need_backticks() {
        assertEquals("a b", transformer.transformName("a$u0020b"));
    }
    
    @Test public void decoded_parts_get_backticks() {
        assertEquals("a.b.`c d`", transformer.transformName("a.b.c$u0020d"));
    }
}
