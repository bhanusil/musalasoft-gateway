create table gateway(
    serial_number varchar(50) NOT NULL,
    name varchar(50) NOT NULL,
    ip_address varchar(50) NOT NUll,
    created datetime NOT NULL,
    updated datetime NOT NULL,
    PRIMARY KEY (serial_number)
);

create table device(
    uid varchar(50) NOT NULL,
    vendor varchar(50) NOT NULL,
    status varchar(50) NOT NUll,
    gateway_serial_number varchar(32) NOT NUll,
    created datetime NOT NULL,
    updated datetime NOT NULL,
    PRIMARY KEY (uid),
    FOREIGN KEY (gateway_serial_number) REFERENCES gateway(serial_number)
);