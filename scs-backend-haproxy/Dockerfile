FROM haproxy:2.3
COPY config.py /tmp/config.py
COPY hello.py /tmp/hello.py
COPY haproxy_conf.cfg /usr/local/etc/haproxy/haproxy.cfg
RUN apt update
RUN apt-get install -y python3
CMD ["/bin/bash", "-c", "/usr/bin/python3 /tmp/config.py && cat /usr/local/etc/haproxy/haproxy.cfg", "haproxy -f /usr/local/etc/haproxy/haproxy.cfg"]