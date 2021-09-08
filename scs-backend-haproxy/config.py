import os

backends_line = os.environ.get('SCS_BACKENDS')
port = os.environ.get('SCS_BACKEND_PORT')

backends = backends_line.split()
config_format = '\n            server %s %s:%s check'

config_block = ''
for server in backends:
    config_block += config_format % (server, server, port)
config_block += '\r\n'

path = '/usr/local/etc/haproxy/haproxy.cfg'

with open(path, 'r') as file:
  filedata = file.read()

filedata = filedata.replace('{$SCS_BACKENDS}', config_block)

with open(path, 'w') as file:
  file.write(filedata)