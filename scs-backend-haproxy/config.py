import os

#backends_line = os.environ.get('SCS_BACKENDS')
#port = os.environ.get('SCS_BACKEND_PORT')
backends_line = 'cdev-backend-1 cdev-backend-2'
port = 8080

backends = backends_line.split()
config_format = 'server %s %s:%d check\n\t'

config_block = ''
for server in backends :
    config_block += config_format % (server, server, port)

path = '/usr/local/etc/haproxy/haproxy.cfg'

with open(path, 'r') as file :
  filedata = file.read()

filedata = filedata.replace('{$SCS_BACKENDS}', config_block)

with open(path, 'w') as file:
  file.write(filedata)