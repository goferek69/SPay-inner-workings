/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.cmp.InfoTypeAndValue;

public class GenMsgContent
extends ASN1Object {
    private ASN1Sequence content;

    private GenMsgContent(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public GenMsgContent(InfoTypeAndValue infoTypeAndValue) {
        this.content = new DERSequence(infoTypeAndValue);
    }

    public GenMsgContent(InfoTypeAndValue[] arrinfoTypeAndValue) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (int i2 = 0; i2 < arrinfoTypeAndValue.length; ++i2) {
            aSN1EncodableVector.add(arrinfoTypeAndValue[i2]);
        }
        this.content = new DERSequence(aSN1EncodableVector);
    }

    public static GenMsgContent getInstance(Object object) {
        if (object instanceof GenMsgContent) {
            return (GenMsgContent)object;
        }
        if (object != null) {
            return new GenMsgContent(ASN1Sequence.getInstance(object));
        }
        return null;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }

    public InfoTypeAndValue[] toInfoTypeAndValueArray() {
        InfoTypeAndValue[] arrinfoTypeAndValue = new InfoTypeAndValue[this.content.size()];
        for (int i2 = 0; i2 != arrinfoTypeAndValue.length; ++i2) {
            arrinfoTypeAndValue[i2] = InfoTypeAndValue.getInstance(this.content.getObjectAt(i2));
        }
        return arrinfoTypeAndValue;
    }
}
