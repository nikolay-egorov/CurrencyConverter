package by.egorov.currency.converter.model.transformer;

import org.simpleframework.xml.transform.Transform;

import java.text.DecimalFormat;

public class DoubleFormatTransformer implements Transform<Double> {

    private DecimalFormat decimalFormat;

    public DoubleFormatTransformer(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    @Override
    public Double read(String value) throws Exception {
        return decimalFormat.parse(value).doubleValue();
    }

    @Override
    public String write(Double value) throws Exception {
        return decimalFormat.format(value);
    }
}