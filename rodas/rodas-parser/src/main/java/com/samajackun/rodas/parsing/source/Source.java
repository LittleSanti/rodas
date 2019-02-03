package com.samajackun.rodas.parsing.source;

import java.io.IOException;

public interface Source
{
	int nextChar()
		throws IOException;

	void startRecord();

	CharSequence endRecord();

	int getCurrentIndex();

	int getCurrentLine();

	int getCurrentColumn();
}