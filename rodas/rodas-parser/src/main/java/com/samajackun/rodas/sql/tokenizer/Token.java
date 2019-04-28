package com.samajackun.rodas.sql.tokenizer;

public class Token
{
	private final String type;

	private final String image;

	private final String value;

	public Token(String type, String image, String value)
	{
		super();
		this.type=type;
		this.image=image;
		this.value=value;
	}

	public Token(String type, String image)
	{
		this(type, image, image);
	}

	public String getType()
	{
		return this.type;
	}

	public String getImage()
	{
		return this.image;
	}

	public String getValue()
	{
		return this.value;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Token other=(Token)obj;
		if (this.image == null)
		{
			if (other.image != null)
			{
				return false;
			}
		}
		else if (!this.image.equals(other.image))
		{
			return false;
		}
		if (this.type != other.type)
		{
			return false;
		}
		if (this.value == null)
		{
			if (other.value != null)
			{
				return false;
			}
		}
		else if (!this.value.equals(other.value))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "Token [type=" + this.type + ", image=" + this.image + ", value=" + this.value + "]";
	}

}