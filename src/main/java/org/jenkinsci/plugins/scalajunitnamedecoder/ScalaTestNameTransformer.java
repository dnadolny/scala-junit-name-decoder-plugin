package org.jenkinsci.plugins.scalajunitnamedecoder;

import org.apache.commons.lang.StringUtils;

import hudson.Extension;
import hudson.tasks.junit.TestNameTransformer;

@Extension
public class ScalaTestNameTransformer extends TestNameTransformer {
    @Override
    public String transformName(String name) {
        String[] parts = StringUtils.split(name, '.');
        if (parts.length == 1) {
            return ScalaNameDecoder.decode(parts[0]);
        }
        for (int i = 0; i < parts.length; i++) {
            String decoded = ScalaNameDecoder.decode(parts[i]);
            if (!decoded.equals(parts[i])) {
                parts[i] = "`" + decoded + "`";
            }
        }
        return StringUtils.join(parts, '.');
    }
}

