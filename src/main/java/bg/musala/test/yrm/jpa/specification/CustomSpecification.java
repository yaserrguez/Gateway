package bg.musala.test.yrm.jpa.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomSpecification<T>
{
    private final Class<T> specificEntityClass;

    public CustomSpecification(Class<T> entityClass)
    {
        this.specificEntityClass = entityClass;
    }

    public Specification<T> getSpecification(String attribute, String valueAttribute)
    {
        return new Specification()
        {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder)
            {
                Path path = root.get(attribute);
                return criteriaBuilder.equal(path, valueAttribute);
            }
        };
    }

    public Specification<T> getSpecificationByAndOperator(List<String> queryList)
    {
        List<Specification<T>> specificationList = new ArrayList<>();

        for (String query : queryList)
        {
            Pattern pattern = Pattern.compile("(\\w*)(=)((\\w-* *\\.*)*)");
            Matcher matcher = pattern.matcher(query);
            while (matcher.find())
            {
                String attribute = matcher.group(1);
                String operator = matcher.group(2);
                String value = matcher.group(3);

                specificationList.add(getSpecification(attribute, value));
            }
        }

        Specification<T> finalSpecification = specificationList.get(0);
        for (int i = 1; i < specificationList.size(); i++)
        {
            finalSpecification = Specification.where(finalSpecification).and(specificationList.get(i));
        }
        return finalSpecification;
    }
}
