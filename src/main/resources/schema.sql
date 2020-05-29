create table  if not exists public.books (

    id serial  primary  key  not null,
    title varchar not null,
    author varchar not null,
    isbn varchar unique not null,
    price float not null
);

create table  if not exists public.inventory (
    id serial  primary key not null,
    no_of_copies int not null,
    book_id int references public.books(id)
);
