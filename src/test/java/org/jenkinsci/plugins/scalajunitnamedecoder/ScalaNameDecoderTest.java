package org.jenkinsci.plugins.scalajunitnamedecoder;

import static org.junit.Assert.*;

import org.junit.Test;

public class ScalaNameDecoderTest {
	@Test public void decode_basic_name() {
		assertEquals("a > b or a < c", ScalaNameDecoder.decode("a$u0020$greater$u0020b$u0020or$u0020a$u0020$less$u0020c"));
	}
	
	@Test public void decode_all_special_symbols() {
        assertEquals("~=<>!#%^&|*/+-:\\?@", ScalaNameDecoder.decode("$tilde$eq$less$greater$bang$hash$percent$up$amp$bar$times$div$plus$minus$colon$bslash$qmark$at"));
    }
	
	@Test public void non_existent_symbols_pass_through() {
        assertEquals("$abc", ScalaNameDecoder.decode("$abc"));
    }
	
	@Test public void decode_unicode() {
        assertEquals(" ", ScalaNameDecoder.decode("$u0020"));
    }
	
	@Test public void non_unicode_passes_through() {
        assertEquals("$u020$0000$u$$$$", ScalaNameDecoder.decode("$u020$0000$u$$$$"));
    }
	
	@Test public void init_decodes_to_this() {
        assertEquals("this", ScalaNameDecoder.decode("<init>"));
    }
}
