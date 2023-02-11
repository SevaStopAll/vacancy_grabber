select person.name as person, company.name as company from person
join company on person.company_id = company.id
where company_id != 5;


select company.name, count(*) as persons from company
join person
on company.id = person.company_id
group by company.name
having count(*) = (select count(*) from  company
                  join person
                  on company.id = person.company_id
                  group by company.name
                  order by 1 desc limit 1);


