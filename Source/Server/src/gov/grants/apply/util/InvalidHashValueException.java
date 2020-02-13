/*
 * Created on Jan 11, 2005
 */
package gov.grants.apply.util;

import java.text.MessageFormat;

/**
 * A custom Exception signifying the hash value in the Header of the Grant
 * application XML does not match the calculated hash value of the received XML.
 * 
 * @author David Wong
 */
public class InvalidHashValueException
    extends Exception
{
    private static final String MESSAGE_TEMPLATE = "The hash value calculated from the Grant application XML does not match the hash value in its header.  The expected value is \"{0}\" but the header has \"{1}\".";

    /**
     * Create the exception initialize with what the expected hash value should
     * have been and what was given in the header of the Grant application XML.
     * 
     * @param calculatedHash
     *            The calculated hash value.
     * @param hashFromHeader
     *            The hash value from the header.
     */
    public InvalidHashValueException(String calculatedHash,
        String hashFromHeader)
    {
        super(MessageFormat.format(MESSAGE_TEMPLATE, _makeArray(calculatedHash,
            hashFromHeader)));
    }

    private static final String[] _makeArray(String calculatedHash,
        String hashFromHeader)
    {
        String[] args = new String[2];
        args[0] = calculatedHash;
        args[1] = hashFromHeader;
        return args;
    }

    public static final void main(String[] args)
    {
        InvalidHashValueException e = new InvalidHashValueException(
            "calculated", "in header");
        e.printStackTrace();
    }
}