package com.samajackun.rodas.core.model;

import java.io.IOException;
import java.io.PrintWriter;

public interface SourceSerializable {
	public void serializeToSource(PrintWriter writer) throws IOException;
}
